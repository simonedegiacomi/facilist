package it.unitn.provolosi.shoppingcart.shoppingcartserver.services.notification

import com.fasterxml.jackson.databind.ObjectMapper
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.Notification
import it.unitn.provolosi.shoppingcart.shoppingcartserver.models.PushSubscription
import nl.martijndwars.webpush.PushService
import nl.martijndwars.webpush.Utils
import org.bouncycastle.jce.ECNamedCurveTable
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.bouncycastle.jce.spec.ECPublicKeySpec
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.security.KeyFactory
import java.security.PublicKey
import java.security.Security


@Component
class PushNotificationDeliveryMethod (
        @Value("\${pushAPI.VAPID.base64PublicKey}")
        private val base64PublicKey: String,

        @Value("\${pushAPI.VAPID.base64PrivateKey}")
        private val base64PrivateKey: String

) : NotificationDeliveryMethod {

    private val pushService = PushService()

    init {
        Security.addProvider(BouncyCastleProvider())
        pushService.publicKey = Utils.loadPublicKey(base64PublicKey)
        pushService.privateKey = Utils.loadPrivateKey(base64PrivateKey)
    }

    override fun canDeliver(notification: Notification) = notification.target.pushSubscriptions.size > 0

    override fun deliver(notification: Notification) {
        val user = notification.target



        user.pushSubscriptions.forEach { subscription ->
            pushService.send(nl.martijndwars.webpush.Notification(
                subscription.endpoint,
                getUserPublicKey(subscription),
                subscription.authAsBytes,
                buildNotificationPayload(notification)
            ))
        }
    }


    fun getUserPublicKey(subscription: PushSubscription): PublicKey {
        val kf = KeyFactory.getInstance("ECDH", BouncyCastleProvider.PROVIDER_NAME)
        val ecSpec = ECNamedCurveTable.getParameterSpec("secp256r1")
        val point = ecSpec.curve.decodePoint(subscription.publicKeyAsBytes)
        val pubSpec = ECPublicKeySpec(point, ecSpec)

        return kf.generatePublic(pubSpec)
    }

    fun buildNotificationPayload(notification: Notification): ByteArray = ObjectMapper().writeValueAsString(mapOf(
        "notification" to mapOf(
            "title" to "Shopping list",
            "body" to notification.message
        )
    )).toByteArray()


}