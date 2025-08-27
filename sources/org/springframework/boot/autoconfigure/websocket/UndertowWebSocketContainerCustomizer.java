package org.springframework.boot.autoconfigure.websocket;

import io.undertow.servlet.api.DeploymentInfo;
import io.undertow.websockets.jsr.WebSocketDeploymentInfo;
import org.springframework.boot.context.embedded.undertow.UndertowDeploymentInfoCustomizer;
import org.springframework.boot.context.embedded.undertow.UndertowEmbeddedServletContainerFactory;

/* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/websocket/UndertowWebSocketContainerCustomizer.class */
public class UndertowWebSocketContainerCustomizer extends WebSocketContainerCustomizer<UndertowEmbeddedServletContainerFactory> {
    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.springframework.boot.autoconfigure.websocket.WebSocketContainerCustomizer
    public void doCustomize(UndertowEmbeddedServletContainerFactory container) {
        WebsocketDeploymentInfoCustomizer customizer = new WebsocketDeploymentInfoCustomizer();
        container.addDeploymentInfoCustomizers(customizer);
    }

    /* loaded from: spring-boot-autoconfigure-1.5.22.RELEASE.jar:org/springframework/boot/autoconfigure/websocket/UndertowWebSocketContainerCustomizer$WebsocketDeploymentInfoCustomizer.class */
    private static class WebsocketDeploymentInfoCustomizer implements UndertowDeploymentInfoCustomizer {
        private WebsocketDeploymentInfoCustomizer() {
        }

        @Override // org.springframework.boot.context.embedded.undertow.UndertowDeploymentInfoCustomizer
        public void customize(DeploymentInfo deploymentInfo) {
            WebSocketDeploymentInfo info = new WebSocketDeploymentInfo();
            deploymentInfo.addServletContextAttribute("io.undertow.websockets.jsr.WebSocketDeploymentInfo", info);
        }
    }
}
