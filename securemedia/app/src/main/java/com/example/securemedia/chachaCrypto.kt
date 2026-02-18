package com.example.securemedia

import java.nio.ByteBuffer
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec

object ChaChaCrypto {

    private const val ITERATIONS = 65536
    private const val KEY_LENGTH = 256

    private fun deriveKey(password: String, salt: ByteArray): SecretKeySpec {
        val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
        val spec = PBEKeySpec(password.toCharArray(), salt, ITERATIONS, KEY_LENGTH)
        val keyBytes = factory.generateSecret(spec).encoded
        return SecretKeySpec(keyBytes, "ChaCha20")
    }

    fun encrypt(data: ByteArray, password: String, extension: String): ByteArray {

        val salt = ByteArray(16)
        val nonce = ByteArray(12)
        SecureRandom().nextBytes(salt)
        SecureRandom().nextBytes(nonce)

        val key = deriveKey(password, salt)
        val cipher = Cipher.getInstance("ChaCha20-Poly1305")
        cipher.init(Cipher.ENCRYPT_MODE, key, GCMParameterSpec(128, nonce))

        val encrypted = cipher.doFinal(data)

        val extBytes = extension.toByteArray()
        val buffer = ByteBuffer.allocate(4 + extBytes.size + salt.size + nonce.size + encrypted.size)

        buffer.putInt(extBytes.size)
        buffer.put(extBytes)
        buffer.put(salt)
        buffer.put(nonce)
        buffer.put(encrypted)

        return buffer.array()
    }

    fun decrypt(data: ByteArray, password: String): Pair<ByteArray, String> {

        val buffer = ByteBuffer.wrap(data)

        val extSize = buffer.int
        val extBytes = ByteArray(extSize)
        buffer.get(extBytes)
        val extension = String(extBytes)

        val salt = ByteArray(16)
        buffer.get(salt)

        val nonce = ByteArray(12)
        buffer.get(nonce)

        val cipherText = ByteArray(buffer.remaining())
        buffer.get(cipherText)

        val key = deriveKey(password, salt)
        val cipher = Cipher.getInstance("ChaCha20-Poly1305")
        cipher.init(Cipher.DECRYPT_MODE, key, GCMParameterSpec(128, nonce))

        val decrypted = cipher.doFinal(cipherText)

        return Pair(decrypted, extension)
    }
}
