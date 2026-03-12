package app.utakata.ac.application;

import com.github.kokorin.jaffree.StreamType;
import com.github.kokorin.jaffree.ffmpeg.FFmpeg;
import com.github.kokorin.jaffree.ffmpeg.UrlInput;
import com.github.kokorin.jaffree.ffmpeg.UrlOutput;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

// ApplicationScoped は Quarkus の DI のアノテーションで、
// アプリケーション全体で一つのインスタンスを使い回す（シングルトン）
@ApplicationScoped
public class VoiceGenerator {
    private static final int SILENT_MSEC = 500;

    private final Path voiceAssetRoot;
    private final Path outputDir;

    public VoiceGenerator(
            @ConfigProperty(name = "app.voice.asset-root") String voiceAssetRoot,
            @ConfigProperty(name = "app.voice.output-dir") String outputDir
    ) {
        this.voiceAssetRoot = Path.of(voiceAssetRoot);
        this.outputDir = Path.of(outputDir);
    }

    /**
     * テキストからどうぶつ語の音声を生成する。
     *
     * @param text 読み上げ対象の文字列
     * @param speed 1秒間に何文字読み上げるか
     * @param voiceType 声の高さ
     */
    public void generate(String text, int speed, VoiceType voiceType) {
        if (text == null || text.isBlank()) {
            return;
        }
        if (speed <= 0) {
            throw new IllegalArgumentException("speed must be greater than 0");
        }

        List<String> voiceParts = getVoiceParts(voiceType);
        Pattern pattern = buildVoicePartPattern(voiceParts);
        List<Segment> segments = createSegments(text, speed, pattern, voiceType);
        if (segments.isEmpty()) {
            return;
        }

        Path outputPath = createOutputPath();
        FFmpeg ffmpeg = FFmpeg.atPath();
        for (Segment segment : segments) {
            ffmpeg.addInput(segment.toInput());
        }

        ffmpeg
                .setOverwriteOutput(true)
                .setComplexFilter(buildFilterGraph(segments.size()))
                .addOutput(
                        UrlOutput.toPath(outputPath)
                                .setFormat("mp3")
                                .setCodec(StreamType.AUDIO, "libmp3lame")
                                .addMap("[outa]")
                )
                .execute();
    }

    private List<Segment> createSegments(String text, int speed, Pattern pattern, VoiceType voiceType) {
        String replacedText = text.replace("っ", "つ");
        int partDurationMillis = 1000 / speed;
        List<Segment> segments = new ArrayList<>();

        while (!replacedText.isEmpty()) {
            Matcher matcher = pattern.matcher(replacedText);
            if (!matcher.find()) {
                segments.add(Segment.silence(SILENT_MSEC));
                replacedText = replacedText.substring(1);
                continue;
            }

            if (matcher.start() != 0) {
                segments.add(Segment.silence(SILENT_MSEC));
            }

            segments.add(Segment.audio(getVoicePartPath(voiceType, matcher.group()), partDurationMillis));
            replacedText = replacedText.substring(matcher.end());
        }
        return segments;
    }

    private List<String> getVoiceParts(VoiceType voiceType) {
        Path voiceDir = getVoiceDir(voiceType);
        try (var paths = Files.list(voiceDir)) {
            return paths
                    .map(path -> path.getFileName().toString())
                    .filter(fileName -> fileName.endsWith(".mp3"))
                    .map(this::removeExtension)
                    .sorted(Comparator.comparingInt(String::length).reversed())
                    .toList();
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load voice parts from " + voiceDir, e);
        }
    }

    private Pattern buildVoicePartPattern(List<String> voiceParts) {
        String pattern = voiceParts.stream()
                .map(Pattern::quote)
                .collect(Collectors.joining("|"));
        return Pattern.compile(pattern);
    }

    private Path getVoiceDir(VoiceType voiceType) {
        return voiceAssetRoot.resolve(voiceType.directoryName());
    }

    private Path getVoicePartPath(VoiceType voiceType, String part) {
        return getVoiceDir(voiceType).resolve(part + ".mp3");
    }

    private Path createOutputPath() {
        try {
            Files.createDirectories(outputDir);
            return outputDir.resolve("animal_crossing_voice" + UUID.randomUUID().toString().replace("-", "") + ".mp3");
        } catch (IOException e) {
            throw new IllegalStateException("Failed to prepare output directory " + outputDir, e);
        }
    }

    private String buildFilterGraph(int inputCount) {
        List<String> chains = new ArrayList<>();
        StringBuilder concatInputs = new StringBuilder();
        for (int i = 0; i < inputCount; i++) {
            chains.add("[" + i + ":a]aresample=44100,aformat=sample_fmts=s16:channel_layouts=mono,asetpts=N/SR/TB[a" + i + "]");
            concatInputs.append("[a").append(i).append("]");
        }
        chains.add(concatInputs + "concat=n=" + inputCount + ":v=0:a=1[outa]");
        return String.join(";", chains);
    }

    private String removeExtension(String fileName) {
        int lastDot = fileName.lastIndexOf('.');
        if (lastDot < 0) {
            return fileName;
        }
        return fileName.substring(0, lastDot);
    }

    private sealed interface Segment permits Segment.AudioSegment, Segment.SilenceSegment {
        UrlInput toInput();

        static Segment audio(Path path, int durationMillis) {
            return new AudioSegment(path, durationMillis);
        }

        static Segment silence(int durationMillis) {
            return new SilenceSegment(durationMillis);
        }

        record AudioSegment(Path path, int durationMillis) implements Segment {
            @Override
            public UrlInput toInput() {
                return UrlInput.fromPath(path)
                        .setDuration(durationMillis)
                        .setCodec(StreamType.AUDIO, "mp3");
            }
        }

        record SilenceSegment(int durationMillis) implements Segment {
            @Override
            public UrlInput toInput() {
                return UrlInput.fromUrl("anullsrc=r=44100:cl=mono")
                        .setFormat("lavfi")
                        .setDuration(durationMillis);
            }
        }
    }
}
