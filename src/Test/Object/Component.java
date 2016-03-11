/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Test.Object;

/**
 * Abstract Class, The most abstract of classes. Can be updated as time goes by.
 *
 * @author Raymond Gao
 */
public class Component {

    public void update(float dtime) {
        throw new UnsupportedOperationException("Unimplemented Abstract");
    }

    public void destroy() {
        throw new UnsupportedOperationException("Unimplemented Abstract");
    }
}
