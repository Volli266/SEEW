export function RPC () {
  this.websocket = null
  this.functions = []
  this.username = null
  this.password = null
  this.port = 8090

  let self = this

  this.register = function (fnName, fn) {
    this.functions[fnName] = fn
  }

  this.receive = function (fnName, params) {
    if (fnName in this.functions) {
      this.functions[fnName](params)
    }
  }

  this.call = function (fnName, params) {
    let data = { fun: fnName, username: this.username, password: this.password, params: params }
    if (!(this.websocket.readyState === this.websocket.OPEN)) {
      this.open(function () {
        self.websocket.send(JSON.stringify(data))
      })
    } else {
      this.websocket.send(JSON.stringify(data))
    }
  }

  this.setUsername = function (username) {
    this.username = username
  }

  this.setPassword = function (password) {
    this.password = password
  }

  this.open = function (onopen) {
    this.websocket = new WebSocket('ws://' + window.location.hostname + ':' + this.port + '/ws/')
    this.websocket.onmessage = this.onmessage
    this.websocket.onclose = this.onclose
    this.websocket.onopen = onopen
    this.websocket.onerror = this.onerror
  }

  this.onmessage = function (msg) {
    let data = JSON.parse(msg.data)
    self.receive(data.fun, data.params)
  }

  this.onclose = function () {
    console.log('Connection closed!')
  }

  this.onerror = function (error) {
    console.error('Websocket error: ' + error)
  }
}
