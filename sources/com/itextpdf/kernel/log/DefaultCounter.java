package com.itextpdf.kernel.log;

import com.itextpdf.io.codec.Base64;
import com.itextpdf.kernel.Version;
import java.nio.charset.StandardCharsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Deprecated
/* loaded from: kernel-7.1.10.jar:com/itextpdf/kernel/log/DefaultCounter.class */
public class DefaultCounter implements ICounter {
    private volatile int count = 0;
    private int level = 0;
    private final int[] repeat = {10000, 5000, 1000};
    private int repeat_level = 10000;
    private Logger logger;
    private static byte[] message_1 = Base64.decode("DQoNCllvdSBhcmUgdXNpbmcgaVRleHQgdW5kZXIgdGhlIEFHUEwuDQoNCklmIHRoaXMgaXMgeW91ciBpbnRlbnRpb24sIHlvdSBoYXZlIHB1Ymxpc2hlZCB5b3VyIG93biBzb3VyY2UgY29kZSBhcyBBR1BMIHNvZnR3YXJlIHRvby4NClBsZWFzZSBsZXQgdXMga25vdyB3aGVyZSB0byBmaW5kIHlvdXIgc291cmNlIGNvZGUgYnkgc2VuZGluZyBhIG1haWwgdG8gYWdwbEBpdGV4dHBkZi5jb20NCldlJ2QgYmUgaG9ub3JlZCB0byBhZGQgaXQgdG8gb3VyIGxpc3Qgb2YgQUdQTCBwcm9qZWN0cyBidWlsdCBvbiB0b3Agb2YgaVRleHQgb3IgaVRleHRTaGFycA0KYW5kIHdlJ2xsIGV4cGxhaW4gaG93IHRvIHJlbW92ZSB0aGlzIG1lc3NhZ2UgZnJvbSB5b3VyIGVycm9yIGxvZ3MuDQoNCklmIHRoaXMgd2Fzbid0IHlvdXIgaW50ZW50aW9uLCB5b3UgYXJlIHByb2JhYmx5IHVzaW5nIGlUZXh0IGluIGEgbm9uLWZyZWUgZW52aXJvbm1lbnQuDQpJbiB0aGlzIGNhc2UsIHBsZWFzZSBjb250YWN0IHVzIGJ5IGZpbGxpbmcgb3V0IHRoaXMgZm9ybTogaHR0cDovL2l0ZXh0cGRmLmNvbS9zYWxlcw0KSWYgeW91IGFyZSBhIGN1c3RvbWVyLCB3ZSdsbCBleHBsYWluIGhvdyB0byBpbnN0YWxsIHlvdXIgbGljZW5zZSBrZXkgdG8gYXZvaWQgdGhpcyBtZXNzYWdlLg0KSWYgeW91J3JlIG5vdCBhIGN1c3RvbWVyLCB3ZSdsbCBleHBsYWluIHRoZSBiZW5lZml0cyBvZiBiZWNvbWluZyBhIGN1c3RvbWVyLg0KDQo=");
    private static byte[] message_2 = Base64.decode("WW91ciBsaWNlbnNlIGhhcyBleHBpcmVkISBZb3UgYXJlIG5vdyB1c2luZyBpVGV4dCB1bmRlciB0aGUgQUdQTC4NCg0KSWYgdGhpcyBpcyB5b3VyIGludGVudGlvbiwgeW91IHNob3VsZCBoYXZlIHB1Ymxpc2hlZCB5b3VyIG93biBzb3VyY2UgY29kZSBhcyBBR1BMIHNvZnR3YXJlIHRvby4NClBsZWFzZSBsZXQgdXMga25vdyB3aGVyZSB0byBmaW5kIHlvdXIgc291cmNlIGNvZGUgYnkgc2VuZGluZyBhIG1haWwgdG8gYWdwbEBpdGV4dHBkZi5jb20NCldlJ2QgYmUgaG9ub3JlZCB0byBhZGQgaXQgdG8gb3VyIGxpc3Qgb2YgQUdQTCBwcm9qZWN0cyBidWlsdCBvbiB0b3Agb2YgaVRleHQgb3IgaVRleHRTaGFycA0KYW5kIHdlJ2xsIGV4cGxhaW4gaG93IHRvIHJlbW92ZSB0aGlzIG1lc3NhZ2UgZnJvbSB5b3VyIGVycm9yIGxvZ3MuDQoNCklmIHRoaXMgd2Fzbid0IHlvdXIgaW50ZW50aW9uLCBwbGVhc2UgY29udGFjdCB1cyBieSBmaWxsaW5nIG91dCB0aGlzIGZvcm06IGh0dHA6Ly9pdGV4dHBkZi5jb20vc2FsZXMgb3IgYnkgY29udGFjdGluZyBvdXIgc2FsZXMgZGVwYXJ0bWVudC4=");

    @Override // com.itextpdf.kernel.log.ICounter
    public void onDocumentRead(long size) {
        plusOne();
    }

    @Override // com.itextpdf.kernel.log.ICounter
    public void onDocumentWritten(long size) {
        plusOne();
    }

    private void plusOne() {
        int i = this.count + 1;
        this.count = i;
        if (i > this.repeat_level) {
            if (Version.isAGPLVersion() || Version.isExpired()) {
                String message = new String(message_1, StandardCharsets.ISO_8859_1);
                if (Version.isExpired()) {
                    message = new String(message_2, StandardCharsets.ISO_8859_1);
                }
                this.level++;
                if (this.level == 1) {
                    this.repeat_level = this.repeat[1];
                } else {
                    this.repeat_level = this.repeat[2];
                }
                if (this.logger == null) {
                    this.logger = LoggerFactory.getLogger(getClass());
                }
                this.logger.info(message);
            }
            this.count = 0;
        }
    }
}
