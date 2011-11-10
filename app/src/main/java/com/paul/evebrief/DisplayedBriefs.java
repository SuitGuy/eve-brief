package com.paul.evebrief;

import java.util.ArrayList;

/**
 * Created by Paul on 07/09/2015.
 */
public class DisplayedBriefs {

    private ArrayList<Brief> briefs;

    public DisplayedBriefs(){
        this.briefs = new ArrayList<Brief>();
    }
    public DisplayedBriefs(ArrayList<Brief> briefs, BriefAdapter ba){
        this.briefs = new ArrayList<Brief>();

        for(Brief b : briefs) {
            this.add(b , ba);
        }

    }

    public boolean add(Brief object, BriefAdapter ba) {
        object.loadImage(ba);
        return briefs.add(object);
    }

    public int size(){return briefs.size();}

    public ArrayList<Brief> getBriefs(){return briefs;}
}
