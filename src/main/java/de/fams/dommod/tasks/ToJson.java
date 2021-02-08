package de.fams.dommod.tasks;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.base.Strings;
import de.fams.dommod.*;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.List;

public class ToJson implements Task {
    public static final ObjectMapper MAPPER = new ObjectMapper();

    @Override
    public void process(DmFile mod, List<String> arguments) {
        BufferedWriter output;
        try {
            if (arguments.size() > 1) {
                System.out.println("output takes only zero or one argument (the output file name)");
                return;
            } if (arguments.isEmpty()) {
                output = new BufferedWriter(new OutputStreamWriter(System.out));
            } else {
                output = new BufferedWriter(new PrintWriter(arguments.get(0)));
            }
            JsonNode json = buildJson(mod);
            output.write(MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(json));
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private JsonNode buildJson(DmFile mod) throws IOException {
        ArrayNode result = MAPPER.createArrayNode();
        ArrayNode currentContent = null;
        for (Command cmd: mod.commands) {
            ObjectNode cmdNode = commandNode(cmd);
            if (currentContent == null) {
                result.add(cmdNode);
            } else {
                currentContent.add(cmdNode);
            }
            if (cmd.name.equals("end")) {
                currentContent = null;
            }
            EntityType entityType = StaticTables.STARTCMD_FOR_NAME.get(cmd.name);
            if (entityType != null) {
                currentContent = cmdNode.putArray("content");
                cmdNode.put("type", entityType.toString());
            }
        }
        if (!Strings.isNullOrEmpty(mod.tailComment)) {
            ObjectNode node = MAPPER.createObjectNode();
            node.put("tailComment", mod.tailComment);
            result.add(node);
        }
        return result;
    }

    private ObjectNode commandNode(Command cmd) {
        ObjectNode result = MAPPER.createObjectNode();
        result.put("name", cmd.name);
        if (!Strings.isNullOrEmpty(cmd.prefixComment)) {
            result.put("prefix", cmd.prefixComment);
        }
        if (!Strings.isNullOrEmpty(cmd.lineComment)) {
            result.put("comment", cmd.lineComment);
        }
        int argCount = 1;
        for (Argument arg: cmd.arguments) {
            String keyname;
            if (arg.type == Argument.Type.NUMBER) {
                keyname = String.format("arg%dnum", argCount);
            } else {
                keyname = String.format("arg%dstr", argCount);
            }
            result.put(keyname, arg.value);
            argCount++;
        }
        return result;
    }

    @Override
    public String description() {
        return "Write mod file in .json format";
    }
}
