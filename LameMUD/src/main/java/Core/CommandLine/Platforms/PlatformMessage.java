package Core.CommandLine.Platforms;

public class PlatformMessage {

    public String GetMessage()
    {
        return message;
    }
    public PlatformMessageHeader GetHeader()
    {
        return header;
    }
    public void SetMessage(String message)
    {
        this.message = message;
    }
    public void SetHeader(PlatformMessageHeader header)
    {
        this.header = header;
    }

    private String message;
    private PlatformMessageHeader header;
}
