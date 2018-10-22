import javax.servlet.http.HttpServletRequest

fun HttpServletRequest.protocolPortAndDomain(): String {
    val protocolAndDomain = "$scheme://$serverName"
    if (serverPort != 80 || serverPort != 443) {
        return "$protocolAndDomain:$serverPort"
    }
    return protocolAndDomain
}
