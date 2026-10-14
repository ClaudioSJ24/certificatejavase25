package otherp;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class TestOther {
    String name;

    public TestOther(String name) {
        this.name = name;
    }



    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("name", name)
                .toString();
    }
}
