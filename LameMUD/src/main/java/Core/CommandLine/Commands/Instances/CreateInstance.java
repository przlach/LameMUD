package Core.CommandLine.Commands.Instances;

import Core.CommandLine.Commands.Command;
import Core.CommandLine.VerifiedMessage;
import Core.Game.Instance.InstancesAPIHandler;

import java.util.regex.Matcher;

public class CreateInstance extends Command {

    @Override
    protected String[] GetPatterns() {

        String[] avaiblePatterns = new String[2];
        avaiblePatterns[0] = "\\/instance create ([^\\s]+) ([^\\s]+)";
        avaiblePatterns[1] = "\\/instance create ([^\\s]+)";

        return avaiblePatterns;
    }

    @Override
    public String GetShortDescription() {
        return "   "; // TODO;
    }

    @Override
    public void ExecuteCommand(VerifiedMessage caller) {

        Matcher currentMatch = this.Match(caller.GetMessage());
        String instanceProposedName = currentMatch.group(1);

        String commandResponse;
        boolean createInstanceResult;

        if(currentMatch.groupCount()==2)
        {
            String proposedPassword = currentMatch.group(2);
            createInstanceResult = InstancesAPIHandler.get().createInstance(instanceProposedName, proposedPassword);
        }
        else
        {
            createInstanceResult = InstancesAPIHandler.get().createInstance(instanceProposedName);
        }

        if(createInstanceResult)
        {
            commandResponse = "Instance " + instanceProposedName + " created successfully.";
        }
        else
        {
            commandResponse = "Could not crate instance " + instanceProposedName + ".";
        }

        caller.Reply(commandResponse);
    }
}
