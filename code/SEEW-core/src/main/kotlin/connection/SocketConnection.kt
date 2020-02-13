package connection

class SocketConnection(private val messageService: MessageService): MessageService {

    override suspend fun sendMessage(socketMessage: SocketMessage) {
        messageService.sendMessage(socketMessage)
    }

}