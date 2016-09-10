package org.trichter.pail;

import com.backtype.hadoop.pail.Pail;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.trichter.pail.model.Login;
import org.trichter.pail.structure.LoginPailStructure;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by guy on 9/10/16.
 */
public class MainRun {

    private static Logger logger = Logger.getLogger(MainRun.class);

    public static void main(String[] args) {

        DateTime now = DateTime.now(DateTimeZone.UTC);

        String pailPath = "/home/guy/login";

        //create pail
        logger.info("Creating new LoginPail in: " + pailPath);
        Pail<Login> loginPail = null;
        try {
            loginPail = Pail.create(pailPath, new LoginPailStructure());
        } catch (IOException e) {
            logger.error("Failed to create pail in: " + pailPath + ". Exception: " + ExceptionUtils.getFullStackTrace(e));
            return;
        }
        logger.info("Created pail successfully: " + pailPath);

        //write items
        logger.info("Writing items of type: " + LoginPailStructure.class.getName() + " to pail: " + pailPath);
        int numLogsWrittenSuccessfully = 0;
        try {
            numLogsWrittenSuccessfully = writeLoginObjects(loginPail, now);
        } catch (Exception e) {
            logger.error("Failed to write pail to: " + pailPath + ". Exception: " + ExceptionUtils.getFullStackTrace(e));
            return;
        }
        logger.info("Wrote " + numLogsWrittenSuccessfully + " to: " + pailPath + " successfully");

        //read pails and assert
        logger.info("Starting to read items from pail: " + pailPath);
        List<Login> logins = readLoginObjects(loginPail);
        logger.info("Read " + logins.size() + " login objects from pail: " + pailPath);

        for (Login login : logins) {
            logger.info(login);
        }

        assert (numLogsWrittenSuccessfully == logins.size());
    }



    private static List<Login> readLoginObjects(Pail<Login> loginPail) {

        List<Login> res = new ArrayList<Login>();

        //Pail implements iterable interface
        for (Login login : loginPail) {
            res.add(login);
        }

        return res;
    }

    private static int writeLoginObjects(Pail<Login> loginPail, DateTime now ) throws IOException {

        List<Login> logins = new ArrayList<Login>();
        logins.add(new Login("Guy", now.minusDays(1).getMillis()));
        logins.add(new Login("Guy", now.getMillis()));

        Pail.TypedRecordOutputStream loginOutputStream = null;

        try {
            loginOutputStream = loginPail.openWrite();
            for (Login login : logins) {
                loginOutputStream.writeObject(login);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (null != loginOutputStream) {
                loginOutputStream.close();
            }
        }

        return logins.size();
    }
}
