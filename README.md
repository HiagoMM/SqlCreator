### Class

```java
  class Empregado{
	@Id
	private Long id;
	private String nome;
	private String sobrenome;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getSobrenome() {
		return sobrenome;
	}
	public void setSobrenome(String sobrenome) {
		this.sobrenome = sobrenome;
	}
}
```
## AbstractDao

### DAO

```java
class EmpregadoDao extends AbstractDAO<Empregado, Long>{

	public EmpregadoDao(Connection conn) {
		super(conn);
	}
  
  // all extra methods
	
}
```
### And lets use

```java
public static void main(String[] args) throws Exception {
		Connection conn = getConnection();
		Empregado hiago = new Empregado();
		EmpregadoDao empDao = new EmpregadoDao(conn);
		hiago.setNome("Hiago");
		hiago.setSobrenome("Marques");
		
		empDao.save(hiago); // save and update 
		empDao.findAll(); // read all
		empDao.findById(1l); // read by id
		empDao.delete(1l); //delete by id
	}
```
## SqlGenerator
### Examples
```java
 public static void main(String[] args) throws Exception {
	
		SqlCreator.generateInsert(Empregado.class);
		SqlCreator.generateDelete(Empregado.class);
		SqlCreator.generateUpdate(Empregado.class);
		SqlCreator.generateSelect(Empregado.class);
		
		SqlCreator.generateInsert(Empregado.class);
		SqlCreator.generateDelete(Empregado.class,"id = 1");
		SqlCreator.generateUpdate(Empregado.class,"id = 1");
		SqlCreator.generateSelect(Empregado.class,"id = 1");
	}
```
