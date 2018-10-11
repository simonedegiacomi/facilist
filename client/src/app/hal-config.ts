import { Injectable } from "@angular/core";
import { ExternalConfiguration, ExternalConfigurationHandlerInterface } from "hal-4-angular";
import { HttpClient } from "@angular/common/http";


@Injectable({
    providedIn: 'root'
})
export class ExternalConfigurationService implements ExternalConfigurationHandlerInterface {


    constructor(
        private httpClient: HttpClient
    ) { }

    deserialize() { }

    serialize(){ }



    getHttp(): HttpClient {
        return this.httpClient;
    }

    getProxyUri(): string {
        return "";
    }

    getRootUri(): string {
        return "/api/";
    }

    getExternalConfiguration(): ExternalConfiguration {

        return null;
    }

    setExternalConfiguration(externalConfiguration: ExternalConfiguration) {
    }

}
