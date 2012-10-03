
package com.mulesoft.demo.mule;

import java.util.Scanner;

import org.mule.api.MuleContext;
import org.mule.api.MuleException;
import org.mule.module.client.MuleClient;

public abstract class AbstractDemo
{
    protected void run() throws Exception
    {
        final MuleContext muleContext = bootstrapMule();
        runDemo();
        waitForEnter();
        terminateMule(muleContext);
    }

    protected abstract void runDemo() throws Exception;

    private static void waitForEnter()
    {
        System.out.println("Hit ENTER to stop...\n");
        new Scanner(System.in).nextLine();
    }

    private MuleContext bootstrapMule() throws MuleException
    {
        System.out.printf("Bootstrapping Mule with config: %s ...", getConfigurationFile());
        final MuleClient muleClient = new MuleClient(getConfigurationFile());
        final MuleContext muleContext = muleClient.getMuleContext();
        muleContext.start();
        System.out.println(" Done!\n");
        return muleContext;
    }

    protected abstract String getConfigurationFile();

    private void terminateMule(final MuleContext muleContext)
    {
        System.out.print("Terminating Mule...");
        muleContext.dispose();
        System.out.println(" Bye!");
        System.exit(0);
    }
}
