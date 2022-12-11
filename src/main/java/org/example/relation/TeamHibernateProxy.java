package org.example.relation;

import lombok.Getter;

public class TeamHibernateProxy extends Team{

    private boolean isLoaded; // boolean의 default값은 false

    @Override
    public String getName() {
        if(isLoaded){
            return super.getName();
        }

        isLoaded = true;
        // -> query 직접 날려서 setName(db에서 가져온 값);
        return super.getName();
    }
}
