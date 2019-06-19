package Core.CommandLine.Commands.Instances;

import Core.CommandLine.Commands.Command;
import Core.CommandLine.VerifiedMessage;
import Core.Game.Instance.Instances;
import Core.Game.Instance.InstancesAPIHandler;

public class ListInstances extends Command {

    @Override
    protected String[] GetPatterns() {

        String[] avaiblePatterns = new String[1];
        avaiblePatterns[0] = "\\/instance list";

        return avaiblePatterns;
    }

    @Override
    public String GetShortDescription() {
        return "TBD";   // TODO
    }

    @Override
    public void ExecuteCommand(VerifiedMessage caller) {

        String commandResponse = "Instances:" + System.getProperty("line.separator");

        StringBuilder builder = new StringBuilder();
        for(String s : InstancesAPIHandler.get().getInstancesNames()) {
            builder.append(s);
            builder.append(System.getProperty("line.separator"));
        }
        commandResponse += builder.toString();

        caller.Reply(commandResponse);

    }
}
