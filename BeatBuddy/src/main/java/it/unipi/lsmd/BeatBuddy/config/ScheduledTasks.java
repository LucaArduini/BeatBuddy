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

    @Scheduled(cron = "0 0 1 * * ?")  // Every day at 1:00 AM
    // 0 in the first position:  Specifies the second when the task will run (0 seconds).
    // 0 in the second position: Specifies the minute when the task will run (0 minutes).
    // 1 in the third position:  Specifies the hour when the task will run (1 AM).
    // * in the fourth position: Specifies the day of the month (any day).
    // * in the fifth position:  Specifies the month (any month).
    // ? in the sixth position:  Specifies the day of the week (no specific day).
    public void dailyDatabaseUpdate() {
        System.out.println(">> START: Database update started.");

        myDatabaseUpdaterService.updateDatabase();

        System.out.println(">> END: Database update ended.");
    }
}

@Service
class MyDatabaseUpdaterService {
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
