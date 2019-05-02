package morsechat;

import java.util.StringTokenizer;

public class MorseTranslator {
    public static String asciiToMorse(String msg) {
        String res = "";
        for(int i=0;i<msg.length();i++) {
            if (msg.charAt(i) != ' ') {
                String m = atm(Character.toUpperCase(msg.charAt(i)));
                if (m != null) {
                    res += m + " ";
                }
            } else {
                res += "/ ";
            }
        }
        
        return res;
    }
    
    public static String morseToAscii(String msg) {
        StringTokenizer st = new StringTokenizer(msg, " ", false);
        
        String res = "";
        
        while(st.hasMoreTokens()) {
            String t = st.nextToken();
            
            if(t.equals("/")) {
                res += " ";
            } else {
                char c = mta(t);
                res += (c=='\0') ? "" : c;
            }
        }
        
        return res;
    }
    
    public static String atm(char a) {
        String ret = null;
        
        for(int i=0;i<chs.length;i++) {
            if(chs[i] == a) {
                ret = mrs[i];
            }
        }
        
        return ret;
    }
    
    public static char mta(String a) {
        char ret = '\0';
        
        for(int i=0;i<mrs.length;i++) {
            if(mrs[i].equals(a)) {
                ret = chs[i];
            }
        }
        
        return ret;
    }
    
    public static char[] chs = {
        'A',
        'B',
        'C',
        'D',
        'E',
        'F',
        'G',
        'H',
        'I',
        'J',
        'K',
        'L',
        'M',
        'N',
        'O',
        'P',
        'Q',
        'R',
        'S',
        'T',
        'U',
        'V',
        'W',
        'X',
        'Y',
        'Z',
        '1',
        '2',
        '3',
        '4',
        '5',
        '6',
        '7',
        '8',
        '9',
        '0',
        '.',
        ',',
        '?',
        '!',
        '\'',
        '"',
        '(',
        ')',
        '&',
        ':',
        ';',
        '/',
        '_',
        '=',
        '+',
        '-',
        '$',
        '@',
        '\n'
    };
    
    public static String[] mrs = {
        ".-",
        "-...",
        "-.-.",
        "-..",
        ".",
        "..-.",
        "--.",
        "....",
        "..",
        ".---",
        "-.-",
        ".-..",
        "--",
        "-.",
        "---",
        ".--.",
        "--.-",
        ".-.",
        "...",
        "-",
        "..-",
        "...-",
        ".--",
        "-..-",
        "-.--",
        "--..",
        ".----",
        "..---",
        "...--",
        "....-",
        ".....",
        "-....",
        "--...",
        "---..",
        "----.",
        "-----",
        ".-.-.-",
        "--..--",
        "..--..",
        "-.-.--",
        ".----.",
        ".-..-.",
        "-.--.",
        "-.--.-",
        ".-...",
        "---...",
        "-.-.-.",
        "-..-.",
        "..--.-",
        "-...-",
        ".-.-.",
        "-....-",
        "...-..-",
        ".--.-.",
        ".-.-"
    };
}
