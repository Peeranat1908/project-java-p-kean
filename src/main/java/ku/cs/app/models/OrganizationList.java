package ku.cs.app.models;

import java.util.*;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;


public class OrganizationList {
    private Map<String, Organization> organizations;

    public OrganizationList() {
        organizations = new TreeMap<>();
    }
    OrganizationList organizationList;
    public void addOrganization(String org, Organization organization) {
        organizations.put(org, organization);
    }

    public void removeOrganization(String org, Organization organization) {
        organizations.remove(org, organization);
    }

    public Organization find(String org) {
        Organization found = organizations.get(org);
        if (found == null) {
            throw new RuntimeException(org + " not found");
        }
        return found;
    }
    public Set<String> getAllOrganization() {
        return organizations.keySet();
    }

    public boolean searchOrganization(String searchString, OrganizationList organizationList){
        for(String org : organizationList.getAllOrganization()){
            if (organizationList.find(org).getOrganizationName().equals(searchString)){
                return true;
            }
        }
        return false;
    }

    public boolean searchCategory(String searchString, OrganizationList organizationList){
        for(String org : organizationList.getAllOrganization()){
            if (organizationList.find(org).getCategory().equals(searchString)){
                return true;
            }
        }
        return false;
    }
}
