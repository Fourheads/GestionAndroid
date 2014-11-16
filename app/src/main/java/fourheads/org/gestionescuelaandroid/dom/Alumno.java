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

        DatoString apellido;
        DatoString nombre;
        DatoString dni;
        DatoString fechaNacimiento;

        public DatoString getApellido() {
            return apellido;
        }

        public void setApellido(DatoString apellido) {
            this.apellido = apellido;
        }

        public DatoString getNombre() {
            return nombre;
        }

        public void setNombre(DatoString nombre) {
            this.nombre = nombre;
        }

        public DatoString getDni() {
            return dni;
        }

        public void setDni(DatoString dni) {
            this.dni = dni;
        }

        public DatoString getFechaNacimiento() {
            return fechaNacimiento;
        }

        public void setFechaNacimiento(DatoString fechaNacimiento) {
            this.fechaNacimiento = fechaNacimiento;
        }

        // nested classes

        public class DatoString {
            String value;

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }
        }

        public class DatoInt {
            int value;

            public int getValue() {
                return value;
            }

            public void setValue(int value) {
                this.value = value;
            }
        }
    }
}
