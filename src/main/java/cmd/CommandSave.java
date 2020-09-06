package cmd;
import Control.TableController;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * save the collection to file .csv
 *
 *
 */

public class CommandSave implements Command {

    @Override
    public String execute(String[] args) {
        if(args != null) {
            try {
                File saveFile = new File(args[0]);
                if(!saveFile.exists()){
                    try {
                        if ((saveFile.createNewFile())) {
                            System.out.println("Save file created");
                        } else {
                            System.out.println("Can`t create file.");
                            return null;
                        }
                    }
                    catch (IOException e){
                        System.out.println("Illegal access, get the right access or try to save with another path.");
                        return null;
                    }
                }
                if (saveFile.canWrite()) {
                    TableController.getCurrentTable().save(new File(args[0]));
                    System.out.println("Collection has been saved");
                }
                else {
                    System.out.println("Can`t write in this file, get the right access...");
                }
            }
            catch (FileNotFoundException e){
                System.out.println("File not found. Try another path.");
            }
        }
        else{
            try {
                File saved = new File("saved.csv");
                if(!saved.exists()){
                    try {
                        if ((saved.createNewFile())) {
                            System.out.println("Save file created");
                        } else {
                            System.out.println("Can`t create save file.");
                            return null;
                        }
                    }
                    catch (IOException e){
                        System.out.println("Illegal Access, try to save with another path.");
                        return null;
                    }
                }
                if (saved.canWrite()) {
                    TableController.getCurrentTable().save(new File("saved.csv"));
                    System.out.println("Collection has been saved");
                }
                else{
                    System.out.println("Can`t write in default file, get the right access...");
                }
            }
            catch (FileNotFoundException e){
                System.out.println("Default save file not found. Try to specify path.");
            }
        }
        return null;
    }

    /**
     * get name of command
     *
     * @return String
     */

    public String toString(){
        return "save";
    }
}