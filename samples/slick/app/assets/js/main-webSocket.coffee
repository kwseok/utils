'use strict'

require ['jquery', 'routes'], ($, routes) ->
  wsUrl = routes.controllers.Application.webSocketWS().webSocketURL()
  socket = new WebSocket(wsUrl)

  log = (msg) -> $('#log').append(msg + '<br/>')

  send = () ->
    msg = JSON.stringify({message: 'test'})
    log('send message : ' + msg)
    socket.send(msg)

  socket.onopen = () -> log('open'); send()
  socket.onclose = () -> log('close')
  socket.onmessage = (e) -> log('receive message : ' + e.data); socket.close()
  return
