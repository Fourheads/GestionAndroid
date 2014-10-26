package fourheads.org.gestionescuelaandroid.dom;

import java.util.List;

/**
 * Created by leandro on 23/10/14.
 */
public class Services {

    List<IsisService> value;
    Object extensions;
    List<RestLink> links;

    public List<IsisService> getValue() {
        return value;
    }

    public void setValue(List<IsisService> value) {
        this.value = value;
    }

    public Object getExtensions() {
        return extensions;
    }

    public void setExtensions(Object extensions) {
        this.extensions = extensions;
    }

    public List<RestLink> getLinks() {
        return links;
    }

    public void setLinks(List<RestLink> links) {
        this.links = links;
    }
}
