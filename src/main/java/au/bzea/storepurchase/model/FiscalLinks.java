package au.bzea.storepurchase.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
public class FiscalLinks {
    private String self;
    private String first;
    private String prev;
    private String next;
    private String last;

}
