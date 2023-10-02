/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package desktopapplication.Model.Strategies;


import okhttp3.HttpUrl;
import okhttp3.Response;

/**
 *
 * @author gabryel
 */
public interface RequestStrategy {
    Response Request( HttpUrl url, String data);
}
