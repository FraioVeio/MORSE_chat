package morsechat;

import java.util.ArrayList;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;

public class MorseThread extends Thread {
    private MorseChat frame;
    private boolean send;
    private String msg;
    private float duration;
    
    
    private byte[] buf;
    private AudioFormat af;
    private SourceDataLine sdl;
    
    public float freq = 1000;
    
    public volatile boolean done;
    public static int treshold;
    
    public MorseThread(MorseChat frame, boolean send, float duration) {
        this.frame = frame;
        this.send = send;
        done = false;
        
        this.duration = duration;
    }
    
    public MorseThread(MorseChat frame, String msg, float duration) {
        this(frame, true, duration);
        
        this.msg = msg;
    }
    
    @Override
    public void run() {
        if(send) {
            // Send
            try {
                buf = new byte[1024];
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
                buf[i%buf.length] = morse.get(i) ? (byte)( Math.sin( angle ) * 100 ) : 0;
                if(i%buf.length == buf.length-1)
                    sdl.write( buf, 0, buf.length );
            }
            sdl.write( buf, 0, morse.size()%1024+1 );
            
            sdl.drain();
            sdl.stop();
            
            
            frame.sendButton.setEnabled(true);
            frame.receiveButton.setEnabled(true);
            frame.pb.setIndeterminate(false);
        } else {
            // Receive
            AudioFormat format = new AudioFormat(44100, 16, 2, true, true);
            DataLine.Info targetInfo = new DataLine.Info(TargetDataLine.class, format);
            
            byte[] rollingmean = new byte[durationIndex(10*2000*(float)Math.PI*1/freq)];    // filter mean micros
            System.out.println();
            int rollingmeanindex = 0;
            
            long timeOld = System.nanoTime();
            long time = -1;
            
            ArrayList<Integer> data = new ArrayList<>();
            
            try {
                TargetDataLine targetLine = (TargetDataLine) AudioSystem.getLine(targetInfo);
                targetLine.open(format);
                targetLine.start();

                int numBytesRead;
                byte[] buffer = new byte[targetLine.getBufferSize() / 5];
                
                boolean on = false;
                boolean onold = false;
                
                int count = 0;
                while(frame.receiveButton.isSelected()) {
                    numBytesRead = targetLine.read(buffer, 0, buffer.length);

                    if(numBytesRead == -1)
                        break;
                    
                    for(int i=0;i<buffer.length;i++) {
                        rollingmean[rollingmeanindex++] = buffer[i];
                        if(rollingmeanindex >= rollingmean.length)
                            rollingmeanindex = 0;
                        int volume = 0;
                        for(byte v:rollingmean) {
                            volume += Math.abs(v);
                        }
                        volume /= rollingmean.length;
                        if(count++ % 1000 == 0)
                            frame.pb.setValue(volume*127/100);
                        
                        on = volume > frame.thresholdSlider.getValue();
                        
                        if(on != onold) {
                            if(on) {
                                time = System.nanoTime()-timeOld;
                                timeOld += time;
                                
                                data.add((int) (time/1000000));
                            } else {
                                time = System.nanoTime()-timeOld;
                                timeOld += time;
                                data.add((int) (time/1000000));
                                for(int y=0;y<rollingmean.length;y++) {
                                    rollingmean[y] = 0;
                                }
                            }
                            
                            dataClean(data);
                            
                            String morse = "";
                            for(int y=1;y<data.size();y++) {
                                int d = data.get(y);
                                
                                if(y%2 != 0) {
                                    // on
                                    if(d > duration/2 && d < duration*2) {
                                        morse += ".";
                                    }
                                    if(d >= duration *2) {
                                        morse += "-";
                                    }
                                    
                                } else {
                                    // off
                                    if(d > duration*4 && d < duration*10)
                                        morse += " ";
                                    if(d >= duration*10)
                                        morse += " / ";
                                    
                                }
                            }
                            frame.statusField.setText(morse);
                            frame.text1.setText(MorseTranslator.morseToAscii(morse));
                            
                            onold = on;
                        }
                    }
                }
                
                targetLine.close();
            } catch (Exception e) {
                    System.err.println(e);
            }
            
            
            frame.sendButton.setEnabled(true);
            frame.pb.setIndeterminate(false);
            frame.pb.setValue(0);
        }
        
        done = true;
    }
    
    public void dataClean(ArrayList<Integer> data) {
        //ArrayList<Integer> old = (ArrayList<Integer>) data.clone();
        
        boolean ok = false;
        while(!ok) {
            ok = true;
            for(int i=0;i<data.size();i++) {
                int d = data.get(i);

                if(d<duration*2/3) {
                    if(i != 0) {
                        data.set(i-1, data.get(i-1) + data.get(i) + ((i+1 < data.size()) ? data.get(i+1) : 0));
                        data.remove(i);
                        if(i+1 < data.size())
                            data.remove(i+1);
                    } else {
                        //data.remove(0);
                    }
                    ok = false;
                    break;
                }
            }
        }
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
