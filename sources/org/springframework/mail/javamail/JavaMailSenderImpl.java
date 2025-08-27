package org.springframework.mail.javamail;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Properties;
import javax.activation.FileTypeMap;
import javax.mail.Address;
import javax.mail.AuthenticationFailedException;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.MailException;
import org.springframework.mail.MailParseException;
import org.springframework.mail.MailPreparationException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.util.Assert;

/* loaded from: spring-context-support-4.3.25.RELEASE.jar:org/springframework/mail/javamail/JavaMailSenderImpl.class */
public class JavaMailSenderImpl implements JavaMailSender {
    public static final String DEFAULT_PROTOCOL = "smtp";
    public static final int DEFAULT_PORT = -1;
    private static final String HEADER_MESSAGE_ID = "Message-ID";
    private Session session;
    private String protocol;
    private String host;
    private String username;
    private String password;
    private String defaultEncoding;
    private FileTypeMap defaultFileTypeMap;
    private Properties javaMailProperties = new Properties();
    private int port = -1;

    public JavaMailSenderImpl() {
        ConfigurableMimeFileTypeMap fileTypeMap = new ConfigurableMimeFileTypeMap();
        fileTypeMap.afterPropertiesSet();
        this.defaultFileTypeMap = fileTypeMap;
    }

    public void setJavaMailProperties(Properties javaMailProperties) {
        this.javaMailProperties = javaMailProperties;
        synchronized (this) {
            this.session = null;
        }
    }

    public Properties getJavaMailProperties() {
        return this.javaMailProperties;
    }

    public synchronized void setSession(Session session) {
        Assert.notNull(session, "Session must not be null");
        this.session = session;
    }

    public synchronized Session getSession() {
        if (this.session == null) {
            this.session = Session.getInstance(this.javaMailProperties);
        }
        return this.session;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getProtocol() {
        return this.protocol;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getHost() {
        return this.host;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getPort() {
        return this.port;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return this.username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return this.password;
    }

    public void setDefaultEncoding(String defaultEncoding) {
        this.defaultEncoding = defaultEncoding;
    }

    public String getDefaultEncoding() {
        return this.defaultEncoding;
    }

    public void setDefaultFileTypeMap(FileTypeMap defaultFileTypeMap) {
        this.defaultFileTypeMap = defaultFileTypeMap;
    }

    public FileTypeMap getDefaultFileTypeMap() {
        return this.defaultFileTypeMap;
    }

    @Override // org.springframework.mail.MailSender
    public void send(SimpleMailMessage simpleMessage) throws MailException {
        send(simpleMessage);
    }

    @Override // org.springframework.mail.MailSender
    public void send(SimpleMailMessage... simpleMessages) throws MailException {
        List<MimeMessage> mimeMessages = new ArrayList<>(simpleMessages.length);
        for (SimpleMailMessage simpleMessage : simpleMessages) {
            MimeMailMessage message = new MimeMailMessage(createMimeMessage());
            simpleMessage.copyTo(message);
            mimeMessages.add(message.getMimeMessage());
        }
        doSend((MimeMessage[]) mimeMessages.toArray(new MimeMessage[mimeMessages.size()]), simpleMessages);
    }

    @Override // org.springframework.mail.javamail.JavaMailSender
    public MimeMessage createMimeMessage() {
        return new SmartMimeMessage(getSession(), getDefaultEncoding(), getDefaultFileTypeMap());
    }

    @Override // org.springframework.mail.javamail.JavaMailSender
    public MimeMessage createMimeMessage(InputStream contentStream) throws MailException {
        try {
            return new MimeMessage(getSession(), contentStream);
        } catch (Exception ex) {
            throw new MailParseException("Could not parse raw MIME content", ex);
        }
    }

    @Override // org.springframework.mail.javamail.JavaMailSender
    public void send(MimeMessage mimeMessage) throws MailException {
        send(mimeMessage);
    }

    @Override // org.springframework.mail.javamail.JavaMailSender
    public void send(MimeMessage... mimeMessages) throws MailException {
        doSend(mimeMessages, null);
    }

    @Override // org.springframework.mail.javamail.JavaMailSender
    public void send(MimeMessagePreparator mimeMessagePreparator) throws MailException {
        send(mimeMessagePreparator);
    }

    @Override // org.springframework.mail.javamail.JavaMailSender
    public void send(MimeMessagePreparator... mimeMessagePreparators) throws MailException {
        try {
            List<MimeMessage> mimeMessages = new ArrayList<>(mimeMessagePreparators.length);
            for (MimeMessagePreparator preparator : mimeMessagePreparators) {
                MimeMessage mimeMessage = createMimeMessage();
                preparator.prepare(mimeMessage);
                mimeMessages.add(mimeMessage);
            }
            send((MimeMessage[]) mimeMessages.toArray(new MimeMessage[mimeMessages.size()]));
        } catch (MailException ex) {
            throw ex;
        } catch (MessagingException ex2) {
            throw new MailParseException((Throwable) ex2);
        } catch (Exception ex3) {
            throw new MailPreparationException(ex3);
        }
    }

    public void testConnection() throws MessagingException {
        Transport transport = null;
        try {
            transport = connectTransport();
            if (transport != null) {
                transport.close();
            }
        } catch (Throwable th) {
            if (transport != null) {
                transport.close();
            }
            throw th;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v28 */
    /* JADX WARN: Type inference failed for: r0v29 */
    /* JADX WARN: Type inference failed for: r0v33 */
    /* JADX WARN: Type inference failed for: r0v46 */
    protected void doSend(MimeMessage[] mimeMessageArr, Object[] objArr) throws MailException {
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        Transport transportConnectTransport = null;
        for (int i = 0; i < mimeMessageArr.length; i++) {
            try {
                if (transportConnectTransport == null || !transportConnectTransport.isConnected()) {
                    if (transportConnectTransport != null) {
                        try {
                            transportConnectTransport.close();
                        } catch (Exception e) {
                        }
                        transportConnectTransport = null;
                    }
                    try {
                        transportConnectTransport = connectTransport();
                    } catch (Exception e2) {
                        for (int i2 = i; i2 < mimeMessageArr.length; i2++) {
                            linkedHashMap.put(objArr != 0 ? objArr[i2] : mimeMessageArr[i2], e2);
                        }
                        throw new MailSendException("Mail server connection failed", e2, linkedHashMap);
                    } catch (AuthenticationFailedException e3) {
                        throw new MailAuthenticationException((Throwable) e3);
                    }
                }
                MimeMessage mimeMessage = mimeMessageArr[i];
                try {
                    if (mimeMessage.getSentDate() == null) {
                        mimeMessage.setSentDate(new Date());
                    }
                    String messageID = mimeMessage.getMessageID();
                    mimeMessage.saveChanges();
                    if (messageID != null) {
                        mimeMessage.setHeader(HEADER_MESSAGE_ID, messageID);
                    }
                    Address[] allRecipients = mimeMessage.getAllRecipients();
                    transportConnectTransport.sendMessage(mimeMessage, allRecipients != null ? allRecipients : new Address[0]);
                } catch (Exception e4) {
                    linkedHashMap.put(objArr != 0 ? objArr[i] : mimeMessage, e4);
                }
            } catch (Throwable th) {
                if (transportConnectTransport != null) {
                    try {
                        transportConnectTransport.close();
                    } catch (Exception e5) {
                        if (!linkedHashMap.isEmpty()) {
                            throw new MailSendException("Failed to close server connection after message failures", e5, linkedHashMap);
                        }
                        throw new MailSendException("Failed to close server connection after message sending", e5);
                    }
                }
                throw th;
            }
        }
        if (transportConnectTransport != null) {
            try {
                transportConnectTransport.close();
            } catch (Exception e6) {
                if (!linkedHashMap.isEmpty()) {
                    throw new MailSendException("Failed to close server connection after message failures", e6, linkedHashMap);
                }
                throw new MailSendException("Failed to close server connection after message sending", e6);
            }
        }
        if (!linkedHashMap.isEmpty()) {
            throw new MailSendException(linkedHashMap);
        }
    }

    protected Transport connectTransport() throws NoSuchProviderException, MessagingException {
        String username = getUsername();
        String password = getPassword();
        if ("".equals(username)) {
            username = null;
            if ("".equals(password)) {
                password = null;
            }
        }
        Transport transport = getTransport(getSession());
        transport.connect(getHost(), getPort(), username, password);
        return transport;
    }

    protected Transport getTransport(Session session) throws NoSuchProviderException {
        String protocol = getProtocol();
        if (protocol == null) {
            protocol = session.getProperty("mail.transport.protocol");
            if (protocol == null) {
                protocol = DEFAULT_PROTOCOL;
            }
        }
        return session.getTransport(protocol);
    }
}
