/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package monitoreodirectorio;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author joseluis
 */
public class MonitoreoDirectorio {

    public static final Logger logger = LogManager.getLogger(MonitoreoDirectorio.class);
    private static final String directorio = "/media/myStorage/joseluis/temp/";

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, InterruptedException {
        
        logger.info("Monitoreando directorio {}",directorio);
        WatchService watchService = FileSystems.getDefault().newWatchService();
        Path path = Paths.get(directorio);
        path.register(watchService, ENTRY_CREATE, ENTRY_MODIFY, ENTRY_DELETE);
        boolean poll = true;
        while (poll) {
            WatchKey key = watchService.take();
            for (WatchEvent<?> event : key.pollEvents()) {
                logger.info("Event kind : {} - File : {}",event.kind(), event.context());
            }
            poll = key.reset();
        }
    }

}