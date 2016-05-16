/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MessageSystem;

import Engine.RSM;
import java.util.HashMap;

/**
 *
 * @author FreakingBarbarians
 */
public class DeallocateMessage extends Message{

    HashMap<String, String> manifest;

    public DeallocateMessage(HashMap<String, String> manifest) {
        this.manifest = manifest;
    }

    public void execute() {
        // ResourceManager
    }
}
