/**
 * Parametros de la aplicación para transferir al plugin.
 * @export
 * @interface PluginParameters
 */
export interface PluginParameters {

    /**
     * URL base del api de rastreo.
     * @type {string}
     * @memberof PluginParameters
     */
    trackingApiBaseUrl?: string;

    /**
     * URL base del api de log.
     * @type {string}
     * @memberof PluginParameters
     */
    logApiBaseUrl?: string;

    /**
     * Tiempo (en segundos) del intervalo para capturar datos del GPS.
     * @type {number}
     * @memberof PluginParameters
     */
    gpsInterval?: number;

    /**
     * Determina si el servicio está en ejecución.
     * @type {number}
     * @memberof PluginParameters
     */
    serviceRunning?: number;

    /**
     * Identificador del portador del dispositivo con GPS (Ej: Id del conductor).
     * @type {number}
     * @memberof PluginParameters
     */
    holderId?: number;

    /**
     * Identificador de la actividad rastreada (Ej: Id del servicio de traslado).
     * @type {number}
     * @memberof PluginParameters
     */
    activityId?: number;

    /**
     * Identificador del interesado de la actividad a filtrar (Ej: Id del Cliente).
     * @type {number}
     * @memberof PluginParameters
     */
    ownerId?: number;

    /**
     * Identificador del estado del portador del dispositivo con GPS.
     * @type {string}
     * @memberof PluginParameters
     */
    holderStatus?: string;

    /**
     * Identificador del estado de la actividad rastreada.
     * @type {string}
     * @memberof PluginParameters
     */
    activityStatus?: string

    /**
     * Fecha/Hora de la última posición capturada del GPS.
     * @type {Date}
     * @memberof PluginParameters
     */
    lastPositionDate?: Date;
    
    /**
     * Última posición capturada del GPS {lat},{lng}.
     * @type {Positions}
     * @memberof PluginParameters
     */
    lastPosition?: Positions;
}

/**
 * Posición del GPS {lat},{lng}.
 * @export
 * @interface Positions
 */
export interface Positions {
    lat: string;
    lng: string;
}

