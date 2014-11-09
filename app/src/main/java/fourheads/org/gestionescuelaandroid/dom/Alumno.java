package fourheads.org.gestionescuelaandroid.dom;

/**
 * Created by leandro on 07/11/14.
 */
public class Alumno {
    Members members;

    public Members getMembers() {
        return members;
    }

    public void setMembers(Members members) {
        this.members = members;
    }


    // nested class
    public class Members{

        Dato apellido;
        Dato nombre;

        public Dato getApellido() {
            return apellido;
        }

        public void setApellido(Dato apellido) {
            this.apellido = apellido;
        }

        public Dato getNombre() {
            return nombre;
        }

        public void setNombre(Dato nombre) {
            this.nombre = nombre;
        }




        // nested classes

        public class Dato{
            String value;

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }
        }

    }
}
