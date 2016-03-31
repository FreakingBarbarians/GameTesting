package OldCode;


import groovy.util.Eval;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *  Quick class to test groovy.
 * @author Raymond Gao
 */
public class GroovyTest {
    
    /**
     * Tests groovy
     * @param args 
     */
    public static void main(String[] args) {
        System.out.println(Eval.me("return 'IvanSmells'"));
    }
    
}
