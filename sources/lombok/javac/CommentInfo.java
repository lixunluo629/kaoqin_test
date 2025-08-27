package lombok.javac;

/* loaded from: lombok-1.16.22.jar:lombok/javac/CommentInfo.SCL.lombok */
public final class CommentInfo {
    public final int pos;
    public final int prevEndPos;
    public final String content;
    public final int endPos;
    public final StartConnection start;
    public final EndConnection end;

    /* loaded from: lombok-1.16.22.jar:lombok/javac/CommentInfo$EndConnection.SCL.lombok */
    public enum EndConnection {
        DIRECT_AFTER_COMMENT,
        AFTER_COMMENT,
        ON_NEXT_LINE
    }

    /* loaded from: lombok-1.16.22.jar:lombok/javac/CommentInfo$StartConnection.SCL.lombok */
    public enum StartConnection {
        START_OF_LINE,
        ON_NEXT_LINE,
        DIRECT_AFTER_PREVIOUS,
        AFTER_PREVIOUS
    }

    public CommentInfo(int prevEndPos, int pos, int endPos, String content, StartConnection start, EndConnection end) {
        this.pos = pos;
        this.prevEndPos = prevEndPos;
        this.endPos = endPos;
        this.content = content;
        this.start = start;
        this.end = end;
    }

    public boolean isJavadoc() {
        return this.content.startsWith("/**") && this.content.length() > 4;
    }

    public String toString() {
        return String.format("%d: %s (%s,%s)", Integer.valueOf(this.pos), this.content, this.start, this.end);
    }
}
