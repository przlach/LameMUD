package Core.CommandLine.Messaging;

public class NoFormatMessage implements FormattedMessage {

    public NoFormatMessage(String msg) {
        this.msg = msg;
    }

    @Override
    public String Get() {
        return msg;
    }

    private String msg;
}
