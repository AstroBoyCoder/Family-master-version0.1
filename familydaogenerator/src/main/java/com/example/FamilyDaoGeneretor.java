package com.example;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class FamilyDaoGeneretor {

    public static void main(String[] args) throws Exception{
        Schema schema = new Schema(1, "com.astroboy.family.GreenDao");
        addUser(schema);
        addFamily(schema);
        new DaoGenerator().generateAll(schema,
                "E:\\Projects\\Family-master-version0.1\\app\\src\\main\\java-gen");
    }
    private static void addUser(Schema schema) {
        Entity User = schema.addEntity("User");
        User.addIdProperty().autoincrement();
        User.addIntProperty("User_ID").notNull().unique();
        User.addStringProperty("User_Name").notNull();
        User.addStringProperty("User_Password").notNull();
        User.addStringProperty("User_Address");
        User.addStringProperty("User_Phone").notNull();
        User.addStringProperty("User_Identity");
        User.addStringProperty("User_Mail");
        User.addStringProperty("User_Sex");
        User.addStringProperty("User_RealName");
        User.addStringProperty("User_Age");
        User.addStringProperty("User_Hobby");
        User.addStringProperty("User_Blood");
    }

    private static void addFamily(Schema schema) {
        Entity Family = schema.addEntity("Family");
        Family.addIdProperty().autoincrement();
        Family.addIntProperty("FM_ID").notNull();
        Family.addIntProperty("User_ID").notNull();
        Family.addStringProperty("FM_Name");
        Family.addStringProperty("FM_Address");
        Family.addStringProperty("Create_Time");
        Family.addStringProperty("Reserved_Tel");
        Family.addIntProperty("Member_Type");
        Family.addIntProperty("Member_ID");
    }

}
