package it.unipi.lsmd.BeatBuddy.config;

import it.unipi.lsmd.BeatBuddy.utilities.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
public class ScheduledTasks {

    @Autowired
    private MyDatabaseUpdaterService myDatabaseUpdaterService;

    @Scheduled(cron = "0 0 3 * * SAT")
    // 0: Specifies the second when the task will run (0 seconds).
    // 0: Specifies the minute when the task will run (0 minutes).
    // 3: Specifies the hour when the task will run (3 AM).
    // *: Specifies the day of the month (any day).
    // *: Specifies the month (any month).
    // SAT: Specifies the day of the week (saturday).
    public void dailyDatabaseUpdate() {
        System.out.println(">> START: Database update started.");

        myDatabaseUpdaterService.updateDatabase();

        System.out.println(">> END: Database update ended.");
    }
}

@Service
class MyDatabaseUpdaterService {

    /**
     * Updates the database by executing a Python script.
     * This method launches a Python3 process to run the database update script.
     * Any output or error from the Python script will be inherited and printed to the console.
     */
    public void updateDatabase() {
        try {
            String pathToPythonScript = Constants.folderName_DatabaseUpdateScript + File.separator + "update_database.py";
            ProcessBuilder processBuilder = new ProcessBuilder("python3", pathToPythonScript);
            processBuilder.inheritIO();
            processBuilder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
