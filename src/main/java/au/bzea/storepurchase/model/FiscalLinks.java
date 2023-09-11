package au.bzea.storepurchase.model;

//import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//@Entity
@Getter @Setter @NoArgsConstructor
public class FiscalLinks {
    private String self;
    private String first;
    private String prev;
    private String next;
    private String last;

}
