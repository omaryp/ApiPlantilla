package pe.plantilla.api.utils.dto;

public class GeneralPageTable extends BasicPageTable {
	
	private String search;
	private int filter;
	private boolean sin_comer;
	private String codigo;
	
	public String getSearch() {
		return search;
	}

	public void setSearch(String search) {
		this.search = search;
	}

	public int getFilter() {
		return filter;
	}

	public void setFilter(int filter) {
		this.filter = filter;
	}

	public boolean isSin_comer() {
		return sin_comer;
	}

	public void setSin_comer(boolean sin_comer) {
		this.sin_comer = sin_comer;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	
}