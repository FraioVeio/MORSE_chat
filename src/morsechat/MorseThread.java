package morsechat;

public class MorseThread extends Thread {
    private boolean send;
    private String msg;
    
    public MorseThread(boolean send) {
        this.send = send;
    }
    
    @Override
    public void run() {
        if(send) {
            // Send
        } else {
            // Receive
        }
    }
}
