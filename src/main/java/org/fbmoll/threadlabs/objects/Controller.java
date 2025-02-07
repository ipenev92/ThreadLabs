package org.fbmoll.threadlabs.objects;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.fbmoll.threadlabs.dto.ConfigurationDTO;
import org.fbmoll.threadlabs.utils.View;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Controller {
    final Model model;
    final View view;

    public Controller() {
        this.model = new Model(this);
        this.view = new View(this);
    }

    public void play(ConfigurationDTO configuration) {
        this.model.setConfiguration(configuration);
        this.model.play();
    }

    public void stop() throws InterruptedException {
        this.model.stop();
    }
}