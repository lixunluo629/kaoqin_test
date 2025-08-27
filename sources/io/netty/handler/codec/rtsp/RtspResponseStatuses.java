package io.netty.handler.codec.rtsp;

import io.netty.handler.codec.http.HttpResponseStatus;
import org.apache.poi.ddf.EscherProperties;

/* loaded from: netty-all-4.1.50.Final.jar:io/netty/handler/codec/rtsp/RtspResponseStatuses.class */
public final class RtspResponseStatuses {
    public static final HttpResponseStatus CONTINUE = HttpResponseStatus.CONTINUE;
    public static final HttpResponseStatus OK = HttpResponseStatus.OK;
    public static final HttpResponseStatus CREATED = HttpResponseStatus.CREATED;
    public static final HttpResponseStatus LOW_STORAGE_SPACE = new HttpResponseStatus(EscherProperties.GEOTEXT__BOLDFONT, "Low on Storage Space");
    public static final HttpResponseStatus MULTIPLE_CHOICES = HttpResponseStatus.MULTIPLE_CHOICES;
    public static final HttpResponseStatus MOVED_PERMANENTLY = HttpResponseStatus.MOVED_PERMANENTLY;
    public static final HttpResponseStatus MOVED_TEMPORARILY = new HttpResponseStatus(302, "Moved Temporarily");
    public static final HttpResponseStatus NOT_MODIFIED = HttpResponseStatus.NOT_MODIFIED;
    public static final HttpResponseStatus USE_PROXY = HttpResponseStatus.USE_PROXY;
    public static final HttpResponseStatus BAD_REQUEST = HttpResponseStatus.BAD_REQUEST;
    public static final HttpResponseStatus UNAUTHORIZED = HttpResponseStatus.UNAUTHORIZED;
    public static final HttpResponseStatus PAYMENT_REQUIRED = HttpResponseStatus.PAYMENT_REQUIRED;
    public static final HttpResponseStatus FORBIDDEN = HttpResponseStatus.FORBIDDEN;
    public static final HttpResponseStatus NOT_FOUND = HttpResponseStatus.NOT_FOUND;
    public static final HttpResponseStatus METHOD_NOT_ALLOWED = HttpResponseStatus.METHOD_NOT_ALLOWED;
    public static final HttpResponseStatus NOT_ACCEPTABLE = HttpResponseStatus.NOT_ACCEPTABLE;
    public static final HttpResponseStatus PROXY_AUTHENTICATION_REQUIRED = HttpResponseStatus.PROXY_AUTHENTICATION_REQUIRED;
    public static final HttpResponseStatus REQUEST_TIMEOUT = HttpResponseStatus.REQUEST_TIMEOUT;
    public static final HttpResponseStatus GONE = HttpResponseStatus.GONE;
    public static final HttpResponseStatus LENGTH_REQUIRED = HttpResponseStatus.LENGTH_REQUIRED;
    public static final HttpResponseStatus PRECONDITION_FAILED = HttpResponseStatus.PRECONDITION_FAILED;
    public static final HttpResponseStatus REQUEST_ENTITY_TOO_LARGE = HttpResponseStatus.REQUEST_ENTITY_TOO_LARGE;
    public static final HttpResponseStatus REQUEST_URI_TOO_LONG = HttpResponseStatus.REQUEST_URI_TOO_LONG;
    public static final HttpResponseStatus UNSUPPORTED_MEDIA_TYPE = HttpResponseStatus.UNSUPPORTED_MEDIA_TYPE;
    public static final HttpResponseStatus PARAMETER_NOT_UNDERSTOOD = new HttpResponseStatus(EscherProperties.LINESTYLE__CRMOD, "Parameter Not Understood");
    public static final HttpResponseStatus CONFERENCE_NOT_FOUND = new HttpResponseStatus(EscherProperties.LINESTYLE__LINETYPE, "Conference Not Found");
    public static final HttpResponseStatus NOT_ENOUGH_BANDWIDTH = new HttpResponseStatus(EscherProperties.LINESTYLE__FILLBLIP, "Not Enough Bandwidth");
    public static final HttpResponseStatus SESSION_NOT_FOUND = new HttpResponseStatus(EscherProperties.LINESTYLE__FILLBLIPNAME, "Session Not Found");
    public static final HttpResponseStatus METHOD_NOT_VALID = new HttpResponseStatus(EscherProperties.LINESTYLE__FILLBLIPFLAGS, "Method Not Valid in This State");
    public static final HttpResponseStatus HEADER_FIELD_NOT_VALID = new HttpResponseStatus(EscherProperties.LINESTYLE__FILLWIDTH, "Header Field Not Valid for Resource");
    public static final HttpResponseStatus INVALID_RANGE = new HttpResponseStatus(EscherProperties.LINESTYLE__FILLHEIGHT, "Invalid Range");
    public static final HttpResponseStatus PARAMETER_IS_READONLY = new HttpResponseStatus(EscherProperties.LINESTYLE__FILLDZTYPE, "Parameter Is Read-Only");
    public static final HttpResponseStatus AGGREGATE_OPERATION_NOT_ALLOWED = new HttpResponseStatus(EscherProperties.LINESTYLE__LINEWIDTH, "Aggregate operation not allowed");
    public static final HttpResponseStatus ONLY_AGGREGATE_OPERATION_ALLOWED = new HttpResponseStatus(EscherProperties.LINESTYLE__LINEMITERLIMIT, "Only Aggregate operation allowed");
    public static final HttpResponseStatus UNSUPPORTED_TRANSPORT = new HttpResponseStatus(EscherProperties.LINESTYLE__LINESTYLE, "Unsupported transport");
    public static final HttpResponseStatus DESTINATION_UNREACHABLE = new HttpResponseStatus(EscherProperties.LINESTYLE__LINEDASHING, "Destination unreachable");
    public static final HttpResponseStatus KEY_MANAGEMENT_FAILURE = new HttpResponseStatus(EscherProperties.LINESTYLE__LINEDASHSTYLE, "Key management failure");
    public static final HttpResponseStatus INTERNAL_SERVER_ERROR = HttpResponseStatus.INTERNAL_SERVER_ERROR;
    public static final HttpResponseStatus NOT_IMPLEMENTED = HttpResponseStatus.NOT_IMPLEMENTED;
    public static final HttpResponseStatus BAD_GATEWAY = HttpResponseStatus.BAD_GATEWAY;
    public static final HttpResponseStatus SERVICE_UNAVAILABLE = HttpResponseStatus.SERVICE_UNAVAILABLE;
    public static final HttpResponseStatus GATEWAY_TIMEOUT = HttpResponseStatus.GATEWAY_TIMEOUT;
    public static final HttpResponseStatus RTSP_VERSION_NOT_SUPPORTED = new HttpResponseStatus(505, "RTSP Version not supported");
    public static final HttpResponseStatus OPTION_NOT_SUPPORTED = new HttpResponseStatus(551, "Option not supported");

    public static HttpResponseStatus valueOf(int code) {
        switch (code) {
            case EscherProperties.GEOTEXT__BOLDFONT /* 250 */:
                return LOW_STORAGE_SPACE;
            case 302:
                return MOVED_TEMPORARILY;
            case EscherProperties.LINESTYLE__CRMOD /* 451 */:
                return PARAMETER_NOT_UNDERSTOOD;
            case EscherProperties.LINESTYLE__LINETYPE /* 452 */:
                return CONFERENCE_NOT_FOUND;
            case EscherProperties.LINESTYLE__FILLBLIP /* 453 */:
                return NOT_ENOUGH_BANDWIDTH;
            case EscherProperties.LINESTYLE__FILLBLIPNAME /* 454 */:
                return SESSION_NOT_FOUND;
            case EscherProperties.LINESTYLE__FILLBLIPFLAGS /* 455 */:
                return METHOD_NOT_VALID;
            case EscherProperties.LINESTYLE__FILLWIDTH /* 456 */:
                return HEADER_FIELD_NOT_VALID;
            case EscherProperties.LINESTYLE__FILLHEIGHT /* 457 */:
                return INVALID_RANGE;
            case EscherProperties.LINESTYLE__FILLDZTYPE /* 458 */:
                return PARAMETER_IS_READONLY;
            case EscherProperties.LINESTYLE__LINEWIDTH /* 459 */:
                return AGGREGATE_OPERATION_NOT_ALLOWED;
            case EscherProperties.LINESTYLE__LINEMITERLIMIT /* 460 */:
                return ONLY_AGGREGATE_OPERATION_ALLOWED;
            case EscherProperties.LINESTYLE__LINESTYLE /* 461 */:
                return UNSUPPORTED_TRANSPORT;
            case EscherProperties.LINESTYLE__LINEDASHING /* 462 */:
                return DESTINATION_UNREACHABLE;
            case EscherProperties.LINESTYLE__LINEDASHSTYLE /* 463 */:
                return KEY_MANAGEMENT_FAILURE;
            case 505:
                return RTSP_VERSION_NOT_SUPPORTED;
            case 551:
                return OPTION_NOT_SUPPORTED;
            default:
                return HttpResponseStatus.valueOf(code);
        }
    }

    private RtspResponseStatuses() {
    }
}
