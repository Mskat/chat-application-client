import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class ChatParticipant {
    private List<PrintWriter> participants = new ArrayList<>();

    public List<PrintWriter> getChatParticipants() {
        return participants;
    }

    public void addChatParticipant(PrintWriter participant) {
        participants.add(participant);
    }
}
