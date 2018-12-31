package renetik.spring.sample

import org.springframework.boot.web.context.WebServerInitializedEvent
import org.springframework.context.ApplicationListener
import org.springframework.context.annotation.Configuration
import java.net.InetAddress

var webServerPort: Int = 0
val webServerBaseURL by lazy { "${InetAddress.getLocalHost().hostName}:$webServerPort" }

@Configuration
class ApplicationListenerWebServerInitialized : ApplicationListener<WebServerInitializedEvent> {
    override fun onApplicationEvent(event: WebServerInitializedEvent) {
        webServerPort = event.webServer.port
    }
}