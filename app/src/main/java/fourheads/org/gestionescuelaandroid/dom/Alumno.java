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

        Apellido apellido;
        Nombre nombre;

        public Apellido getApellido() {
            return apellido;
        }

        public void setApellido(Apellido apellido) {
            this.apellido = apellido;
        }

        public Nombre getNombre() {
            return nombre;
        }

        public void setNombre(Nombre nombre) {
            this.nombre = nombre;
        }




        // nested classes
        public class Apellido{
            String value;

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }
        }

        public class Nombre{
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
