package app.utakata.ac.application;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

public final class VoicePartAssets {
    // App Engine にデプロイするときに code build を使ったら、Code Build が日本語ファイルに対応していなかった
    // よって、ファイル名はローマ字にして、ここで日本語とファイル名の対応を持つことにする
    private static final Map<String, String> PART_TO_FILE_NAME = Map.ofEntries(
            Map.entry("あ", "a"),
            Map.entry("い", "i"),
            Map.entry("う", "u"),
            Map.entry("うぃ", "wi"),
            Map.entry("うぇ", "we"),
            Map.entry("うぉ", "uwo"),
            Map.entry("え", "e"),
            Map.entry("お", "o"),
            Map.entry("か", "ka"),
            Map.entry("が", "ga"),
            Map.entry("き", "ki"),
            Map.entry("きゃ", "kya"),
            Map.entry("きゅ", "kyu"),
            Map.entry("きょ", "kyo"),
            Map.entry("ぎ", "gi"),
            Map.entry("ぎゃ", "gya"),
            Map.entry("ぎゅ", "gyu"),
            Map.entry("ぎょ", "gyo"),
            Map.entry("く", "ku"),
            Map.entry("ぐ", "gu"),
            Map.entry("け", "ke"),
            Map.entry("げ", "ge"),
            Map.entry("こ", "ko"),
            Map.entry("ご", "go"),
            Map.entry("さ", "sa"),
            Map.entry("ざ", "za"),
            Map.entry("し", "shi"),
            Map.entry("しぇ", "she"),
            Map.entry("しゃ", "sha"),
            Map.entry("しゅ", "shu"),
            Map.entry("しょ", "sho"),
            Map.entry("じ", "ji"),
            Map.entry("じぇ", "je"),
            Map.entry("じゃ", "ja"),
            Map.entry("じゅ", "ju"),
            Map.entry("じょ", "jo"),
            Map.entry("す", "su"),
            Map.entry("ず", "zu"),
            Map.entry("せ", "se"),
            Map.entry("ぜ", "ze"),
            Map.entry("そ", "so"),
            Map.entry("ぞ", "zo"),
            Map.entry("た", "ta"),
            Map.entry("だ", "da"),
            Map.entry("ち", "chi"),
            Map.entry("ちぇ", "che"),
            Map.entry("ちゃ", "cha"),
            Map.entry("ちゅ", "chu"),
            Map.entry("ちょ", "cho"),
            Map.entry("ぢ", "dji"),
            Map.entry("つ", "tsu"),
            Map.entry("つぁ", "tsa"),
            Map.entry("つぃ", "tsi"),
            Map.entry("つぇ", "tse"),
            Map.entry("つぉ", "tso"),
            Map.entry("づ", "dzu"),
            Map.entry("て", "te"),
            Map.entry("てぃ", "ti"),
            Map.entry("で", "de"),
            Map.entry("でぃ", "di"),
            Map.entry("でゅ", "dyu"),
            Map.entry("と", "to"),
            Map.entry("ど", "do"),
            Map.entry("な", "na"),
            Map.entry("に", "ni"),
            Map.entry("にゃ", "nya"),
            Map.entry("にゅ", "nyu"),
            Map.entry("にょ", "nyo"),
            Map.entry("ぬ", "nu"),
            Map.entry("ね", "ne"),
            Map.entry("の", "no"),
            Map.entry("は", "ha"),
            Map.entry("ば", "ba"),
            Map.entry("ぱ", "pa"),
            Map.entry("ひ", "hi"),
            Map.entry("ひゃ", "hya"),
            Map.entry("ひゅ", "hyu"),
            Map.entry("ひょ", "hyo"),
            Map.entry("び", "bi"),
            Map.entry("びゃ", "bya"),
            Map.entry("びゅ", "byu"),
            Map.entry("びょ", "byo"),
            Map.entry("ぴ", "pi"),
            Map.entry("ぴゃ", "pya"),
            Map.entry("ぴゅ", "pyu"),
            Map.entry("ぴょ", "pyo"),
            Map.entry("ふ", "fu"),
            Map.entry("ふぁ", "fa"),
            Map.entry("ふぃ", "fi"),
            Map.entry("ふぇ", "fe"),
            Map.entry("ふぉ", "fo"),
            Map.entry("ふゅ", "fyu"),
            Map.entry("ぶ", "bu"),
            Map.entry("ぷ", "pu"),
            Map.entry("へ", "he"),
            Map.entry("べ", "be"),
            Map.entry("ぺ", "pe"),
            Map.entry("ほ", "ho"),
            Map.entry("ぼ", "bo"),
            Map.entry("ぽ", "po"),
            Map.entry("ま", "ma"),
            Map.entry("み", "mi"),
            Map.entry("みゃ", "mya"),
            Map.entry("みゅ", "myu"),
            Map.entry("みょ", "myo"),
            Map.entry("む", "mu"),
            Map.entry("め", "me"),
            Map.entry("も", "mo"),
            Map.entry("や", "ya"),
            Map.entry("ゆ", "yu"),
            Map.entry("よ", "yo"),
            Map.entry("ら", "ra"),
            Map.entry("り", "ri"),
            Map.entry("りゃ", "rya"),
            Map.entry("りゅ", "ryu"),
            Map.entry("りょ", "ryo"),
            Map.entry("る", "ru"),
            Map.entry("れ", "re"),
            Map.entry("ろ", "ro"),
            Map.entry("わ", "wa"),
            Map.entry("を", "wo"),
            Map.entry("ん", "n"),
            Map.entry("ゔぁ", "va"),
            Map.entry("ゔぃ", "vi"),
            Map.entry("ゔぇ", "ve"),
            Map.entry("ゔぉ", "vo")
    );

    private static final List<String> SUPPORTED_PARTS = PART_TO_FILE_NAME.keySet().stream()
            .sorted(Comparator.comparingInt(String::length).reversed())
            .toList();

    private VoicePartAssets() {
    }

    public static List<String> supportedParts() {
        return SUPPORTED_PARTS;
    }

    public static String fileNameFor(String part) {
        String fileName = PART_TO_FILE_NAME.get(part);
        if (fileName == null) {
            throw new IllegalArgumentException("Unsupported voice part: " + part);
        }
        return fileName;
    }
}
