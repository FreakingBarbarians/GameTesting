/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package MessageSystem;

import Engine.ResourceManager;

/**
 * Sent when a resource is no longer being used by a game object.
 *
 * @author Raymond Gao
 */
public class ResourceDestroyedMessage extends Message {

    private String resourceID;

    public void execute() {
        ResourceManager.deallocate(resourceID);
    }
}
