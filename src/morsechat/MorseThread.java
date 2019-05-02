package morsechat;

import java.util.ArrayList;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.SourceDataLine;

public class MorseThread extends Thread {
    private MorseChat frame;
    private boolean send;
    private String msg;
    private float duration;
    
    
    private byte[] buf;
    private AudioFormat af;
    private SourceDataLine sdl;
    
    public volatile boolean done;
    
    private MorseThread(MorseChat frame, boolean send) {
        this.frame = frame;
        this.send = send;
        done = false;
    }
    
    public MorseThread(MorseChat frame, String msg, float duration) {
        this(frame, true);
        
        this.msg = msg;
        this.duration = duration;
    }
    
    @Override
    public void run() {
        float freq = 1000;
        
        if(send) {
            // Send
            try {
                buf = new byte[ 1024 ];
                af = new AudioFormat( (float )44100, 8, 1, true, false );
                sdl = AudioSystem.getSourceDataLine( af );
                sdl.open();
                sdl.start();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            
            ArrayList<Boolean> morse = new ArrayList<>();
            
            for(int i=0;i<msg.length();i++) {
                char c = msg.charAt(i);
                
                if(c == '.') {
                    for(int j=0;j<durationIndex(duration*1);j++) {
                        morse.add(true);
                    }
                }
                
                if(c == '-') {
                    for(int j=0;j<durationIndex(duration*3);j++) {
                        morse.add(true);
                    }
                }
                
                if(c == ' ') {
                    for(int j=0;j<durationIndex(duration*5);j++) {
                        morse.add(false);
                    }
                }
                
                if(c == '/') {
                    for(int j=0;j<durationIndex(duration*11);j++) {
                        morse.add(false);
                    }
                }
                
                for(int j=0;j<durationIndex(duration*1);j++) {
                    morse.add(false);
                }
            }
            
            for(int i=0;i<morse.size();i++) {
                double angle = i / ( (float )44100 / freq ) * 2.0 * Math.PI;
                buf[i%1024] = morse.get(i) ? (byte)( Math.sin( angle ) * 100 ) : 0;
                if(i%1024 == 1023)
                    sdl.write( buf, 0, 1024 );
            }
            sdl.write( buf, 0, morse.size()%1024+1 );
            
            sdl.drain();
            sdl.stop();
            
            
            frame.sendButton.setEnabled(true);
            frame.receiveButton.setEnabled(true);

            frame.pb.setIndeterminate(false);
        } else {
            // Receive
            
        }
        
        done = true;
    }
    
    public static int durationIndex(float duration) {
        return Math.round(duration * (float )44100 / 1000);
    }
    
    public static void wait(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ex) {}
    }
    
    public static void wait(float millis) {
        try {
            Thread.sleep(Math.round(millis));
        } catch (InterruptedException ex) {}
    }
}
