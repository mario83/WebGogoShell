package it.manza.portlet;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.net.telnet.TelnetClient;
import org.apache.commons.net.telnet.TerminalTypeOptionHandler;

public class TelnetClientExample implements Runnable
{
    static TelnetClient tc = null;

    public static void main(String[] args) throws Exception
    {
        String remoteip = "localhost";

        int remoteport = 11311;

        tc = new TelnetClient();

        TerminalTypeOptionHandler ttopt = new TerminalTypeOptionHandler("VT100", false, false, true, false);
        tc.addOptionHandler(ttopt);

        while (true)
        {
            boolean end_loop = false;
            try
            {
                tc.connect(remoteip, remoteport);

                Thread reader = new Thread (new TelnetClientExample());
                
                reader.start();
                OutputStream outstr = tc.getOutputStream();

                byte[] buff = new byte[1024];
                int ret_read = 0;

                do
                {
                    try
                    {
                        ret_read = System.in.read(buff);
                        if(ret_read > 0)
                        {
                            try
                            {
                                    outstr.write(buff, 0 , ret_read);
                                    outstr.flush();
                            }
                            catch (IOException e)
                            {
                                    end_loop = true;
                            }
                        }
                    }
                    catch (IOException e)
                    {
                        System.err.println("Exception while reading keyboard:" + e.getMessage());
                        end_loop = true;
                    }
                }
                while((ret_read > 0) && (end_loop == false));

                try
                {
                    tc.disconnect();
                }
                catch (IOException e)
                {
                          System.err.println("Exception while connecting:" + e.getMessage());
                }
            }
            catch (IOException e)
            {
                    System.err.println("Exception while connecting:" + e.getMessage());
                    System.exit(1);
            }
        }
    }


    

    /***
     * Reader thread.
     * Reads lines from the TelnetClient and echoes them
     * on the screen.
     ***/
    @Override
    public void run()
    {
        InputStream instr = tc.getInputStream();

        try
        {
            byte[] buff = new byte[1024];
            int ret_read = 0;

            do
            {
                ret_read = instr.read(buff);
                if(ret_read > 0)
                {
                    System.out.print(new String(buff, 0, ret_read));
                }
            }
            while (ret_read >= 0);
        }
        catch (IOException e)
        {
            System.err.println("Exception while reading socket:" + e.getMessage());
        }

        try
        {
            tc.disconnect();
        }
        catch (IOException e)
        {
            System.err.println("Exception while closing telnet:" + e.getMessage());
        }
    }
}


