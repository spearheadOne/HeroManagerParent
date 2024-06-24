package org.abondar.industrial.heromanager.controller;

public class EndpointUtil {

    public static final String API_VERSION = "/v1";
    public static final String API_ROOT = "/hero";

    public static final String ID_PATH = "/{id}";
    public static final String NAME_PATH= "/name/{name}";
    public static final String ALIAS_PATH= "/alias/{alias}";
    public static final String PROPERTY_PATH= "/{prop}";
    public static final String HERO_PROP_PATH= "/type/{type}/value/{value}";

    public static final String DELETE_PROP_PATH= "/prop" + ID_PATH + PROPERTY_PATH;


    public static final String API_V1_ROOT = API_VERSION + API_ROOT;

    private EndpointUtil() {}
}
