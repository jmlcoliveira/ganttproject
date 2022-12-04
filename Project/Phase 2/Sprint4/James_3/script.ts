/**
 * @author James Hertz
 * This script was used to generate the tables in dependencies.xlsx
 * It will probably be included in the file but, I included here just for precaution
 */

const VALUE_COL = 'J'
const DATA_COL = 'I'
const MAX_ROW = 7075 // the number of 
const SPACE_BETWEEN_TABLES = 3
const CATEGORY_COL = 4
const NAME_COL = 5 // sub category :)

type app_func = (start: number, end: number, cell?: number[]) => string

const TABLE_HEADINGS = [
    'Metrics', "Average",
    "Maximum value", "Maximum count",
    "Minimum value", "Minimum count"
]

function buildRanges(work: ExcelScript.Worksheet, height: number): object {

    const ranges = {}

    for (let i = 1; i < height; i++) {
        const cell = work.getCell(i, CATEGORY_COL)
        const innerCell = work.getCell(i, NAME_COL)

        const innerKey = innerCell.getText()
        const cellKey = cell.getText()
        const cellNum = i + 1

        let category: object = ranges[cellKey]

        if (!category) category = ranges[cellKey] = {}

        if (!category[innerKey]) {
            category[innerKey] = {
                start: cellNum,
                end: cellNum
            }
        } else
            category[innerKey].end = cellNum

    }


    return ranges
}

function getDataRange(row1: number, row2: number): string {
    return `${DATA_COL}${row1}:${DATA_COL}${row2}`
}

function getValueRange(row1: number, row2: number): string {
    return `${VALUE_COL}${row1}:${VALUE_COL}${row2}`
}

function getRealCol(col: number): string {
    return String.fromCharCode(65 + col)
}


const valueCount: app_func = function (start, end, [row, col]) {
    const data_range = getDataRange(start, end)
    const value_range = getValueRange(start, end) // lookup_range

    const target_cell = `${getRealCol(col - 1)}${row + 1}`;

    const count = `COUNTIF(${value_range}, ${target_cell})`

    const search = `INDEX(${data_range}, MATCH(${target_cell}, ${value_range}, 0), 1)`

    return `=IF(${count}=1, ${search}, ${count})`
}


// the functions that will be applied to each one of the subCategories
const TABLE_FUNCS: app_func[] = [
    (start, end) => `=AVERAGE(${getValueRange(start, end)})`,
    (start, end) => `=MAX(${getValueRange(start, end)})`,
    valueCount,
    (start, end) => `=MIN(${getValueRange(start, end)})`,
    valueCount
]


function buildTable(work: ExcelScript.Worksheet, title: string, ranges: object, [row, col]: number[]): number {

    const init_row = row
    const init_col = col
    const category : object = ranges[title]

    const titleCell = work.getCell(row, col)
    titleCell.setValue(title)

    row++

    // building the headers
    for (const header of TABLE_HEADINGS) {
        const cell = work.getCell(row, col++)
        cell.setValue(header)
    }

    row++

    // build the rest of the table :)
    for (let subCat in category) {
        col = init_col

        // sets the sub category header
        work.getCell(row, col++).setValue(subCat)

        for (let func of TABLE_FUNCS) {

            const { start, end } = category[subCat]

            work.getCell(row, col).setValue(
                func(start, end, [row, col++])
            )
        }
        
        row++
    }

    // returns the number of col that we advanced :)
    return row - init_row
}


function main(workbook: ExcelScript.Workbook) {
    const work = workbook.getWorksheets()[0]


    console.log('building ranges...')

    const ranges = buildRanges(work, MAX_ROW)

    console.log('ranges built\n\n')

    console.log('filling rows...\n\n')

    const col = 11
    let row = 50 // I used 11 to generate it, I just changed this in case you want to run it. Just to not mess with with what I've done.

    for (let title in ranges) {
        console.log('processing:', title)
        row += buildTable(work, title, ranges, [row, col]) + SPACE_BETWEEN_TABLES
    }


}