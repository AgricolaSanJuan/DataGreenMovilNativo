package com.example.datagreenmovil.DAO.Evaluaciones.MstMedible;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.example.datagreenmovil.DAO.Evaluaciones.MstEstados.MstEstados;
import com.example.datagreenmovil.DAO.Evaluaciones.MstTipoEvaluaciones.MstTipoEvaluacion;
import com.example.datagreenmovil.DAO.Evaluaciones.MstUnidadesMedida.MstUnidadesMedida;
import com.example.datagreenmovil.DAO.Usuarios.Usuario;

@Entity(tableName = "mst_medible",
		foreignKeys = {
				@ForeignKey(entity = MstMedible.class, parentColumns = "Id", childColumns = "IdCategoriaMedible"),
				@ForeignKey(entity = MstMedible.class, parentColumns = "Id", childColumns = "IdEstadioMedible"),
				@ForeignKey(entity = MstMedible.class, parentColumns = "Id", childColumns = "IdGrupoMedible"),
				@ForeignKey(entity = MstTipoEvaluacion.class, parentColumns = "Id", childColumns = "IdTipoEvaluacion"),
				@ForeignKey(entity = MstEstados.class, parentColumns = "Id", childColumns = "IdEstado"),
				@ForeignKey(entity = MstUnidadesMedida.class, parentColumns = "Id", childColumns = "IdUnidadMedida"),
				@ForeignKey(entity = Usuario.class, parentColumns = "Id", childColumns = "IdUsuarioRegistra"),
				@ForeignKey(entity = Usuario.class, parentColumns = "Id", childColumns = "IdUsuarioActualiza")
		})
public class MstMedible {
	@PrimaryKey
	@NonNull private String Id;
	@NonNull private String Dex ;
	@NonNull private String IdTipoEvaluacion ;
	@NonNull private String IdUnidadMedida ;
	@NonNull private String IdEstado ;
	@NonNull private String IdUsuarioRegistra;
	@NonNull private String Registro ;
	@NonNull private String IdUsuarioActualiza;
	@NonNull private String Actualizacion ;
	@NonNull private String dEstadioMedible ;
	@NonNull private String IdCategoriaMedible;
	@NonNull private String IdOrdenTaxonomicoMedible;
	@NonNull private String IdGrupoMedible;

	@NonNull
	public String getId() {
		return Id;
	}

	public void setId(@NonNull String id) {
		Id = id;
	}

	@NonNull
	public String getDex() {
		return Dex;
	}

	public void setDex(@NonNull String dex) {
		Dex = dex;
	}

	@NonNull
	public String getIdTipoEvaluacion() {
		return IdTipoEvaluacion;
	}

	public void setIdTipoEvaluacion(@NonNull String idTipoEvaluacion) {
		IdTipoEvaluacion = idTipoEvaluacion;
	}

	@NonNull
	public String getIdUnidadMedida() {
		return IdUnidadMedida;
	}

	public void setIdUnidadMedida(@NonNull String idUnidadMedida) {
		IdUnidadMedida = idUnidadMedida;
	}

	@NonNull
	public String getIdEstado() {
		return IdEstado;
	}

	public void setIdEstado(@NonNull String idEstado) {
		IdEstado = idEstado;
	}

	@NonNull
	public String getIdUsuarioRegistra() {
		return IdUsuarioRegistra;
	}

	public void setIdUsuarioRegistra(@NonNull String idUsuarioRegistra) {
		IdUsuarioRegistra = idUsuarioRegistra;
	}

	@NonNull
	public String getRegistro() {
		return Registro;
	}

	public void setRegistro(@NonNull String registro) {
		Registro = registro;
	}

	@NonNull
	public String getIdUsuarioActualiza() {
		return IdUsuarioActualiza;
	}

	public void setIdUsuarioActualiza(@NonNull String idUsuarioActualiza) {
		IdUsuarioActualiza = idUsuarioActualiza;
	}

	@NonNull
	public String getActualizacion() {
		return Actualizacion;
	}

	public void setActualizacion(@NonNull String actualizacion) {
		Actualizacion = actualizacion;
	}

	@NonNull
	public String getdEstadioMedible() {
		return dEstadioMedible;
	}

	public void setdEstadioMedible(@NonNull String dEstadioMedible) {
		this.dEstadioMedible = dEstadioMedible;
	}

	@NonNull
	public String getIdCategoriaMedible() {
		return IdCategoriaMedible;
	}

	public void setIdCategoriaMedible(@NonNull String idCategoriaMedible) {
		IdCategoriaMedible = idCategoriaMedible;
	}

	@NonNull
	public String getIdOrdenTaxonomicoMedible() {
		return IdOrdenTaxonomicoMedible;
	}

	public void setIdOrdenTaxonomicoMedible(@NonNull String idOrdenTaxonomicoMedible) {
		IdOrdenTaxonomicoMedible = idOrdenTaxonomicoMedible;
	}

	@NonNull
	public String getIdGrupoMedible() {
		return IdGrupoMedible;
	}

	public void setIdGrupoMedible(@NonNull String idGrupoMedible) {
		IdGrupoMedible = idGrupoMedible;
	}
}

