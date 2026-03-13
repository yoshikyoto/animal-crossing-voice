<template>
  <form :action="voiceGenerateUrl">
    <div>
      <textarea name="text" rows="8" cols="100" placeholder="しゃべらせたいことばをひらがなでにゅうりょく"></textarea>
    </div>
    <div class="animal-box">
      速度:
      <input type="number" name="speed" value="10" class="text-input">
      （1秒に何文字読むか）
    </div>
    <div class="animal-box">
      声の種類:
      <input type="radio" name="type" value="high" checked>高い
      <input type="radio" name="type" value="highhigh">すごく高い
    </div>
    <div class="animal-box">
      <button type="submit" class="button button-submit">生成</button>
    </div>
  </form>
</template>

<script>
export default {
  name: 'App',
  data() {
    return {
      text: "",
      apiBaseUrl: "https://api-pokemon.utakata.app"
    }
  },
  mounted() {
    // 起動時に ping url を叩いて Heroku を起動させる
    this.axios.get(this.pingUrl)
  },
  computed: {
    voiceGenerateUrl() {
      return this.apiBaseUrl + "/animal_crossing_voice"
    },
    pingUrl() {
      return this.apiBaseUrl + "/ping"
    }
  }
}
</script>

<style>
body {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}

h2 {
  margin-top: 20px;
  margin-bottom: 5px;
}

.button-submit {
  -webkit-box-sizing: inherit;
  box-sizing: inherit;
  font-size: 1.6rem;
  font-weight: 700;
  padding: 4px 20px;
  cursor: pointer;
  -webkit-user-select: none;
  -moz-user-select: none;
  -ms-user-select: none;
  user-select: none;
  text-align: center;
  vertical-align: middle;
  text-decoration: none;
  letter-spacing: 0.1em;
  border-radius: 0.5rem;
}

.button-submit {
  color: #ffffff;
  background-color: #2ca559;
}

.animal-box {
  padding: 5px;
}

.text-input {
  width: 80px;
  height: 30px;
  font-size: 1.2em;
}
</style>
