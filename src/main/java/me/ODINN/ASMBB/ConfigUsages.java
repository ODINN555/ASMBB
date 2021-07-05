package me.ODINN.ASMBB;

public enum ConfigUsages {

    NotPlayer("NotPlayer"),
    NotEnoughArguments("NotEnoughArguments"),
    TooManyArguments("TooManyArguments"),
    IncompatibleArea("IncompatibleArea"),
    NameAlreadyExists("NameAlreadyExists")
    ;


    private final String usage;

    /**
     *
     * @param usage the usage in the config
     */
    ConfigUsages(String usage){
        this.usage = usage;
    }

    public String getUsage(){
        return this.usage;
    }
}
