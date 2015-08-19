import geb.spock.GebSpec

class TmpSpec extends GebSpec {

    void 'tmp case'(){
        go "http://localhost:8080"
        sleep(100)

        expect:
        true
    }
}